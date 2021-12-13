import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBusRoute, BusRoute } from '../bus-route.model';
import { BusRouteService } from '../service/bus-route.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

@Component({
  selector: 'jhi-bus-route-update',
  templateUrl: './bus-route-update.component.html',
})
export class BusRouteUpdateComponent implements OnInit {
  isSaving = false;

  studentsSharedCollection: IStudent[] = [];

  editForm = this.fb.group({
    id: [],
    routeName: [],
    routeDriver: [],
    busNumber: [],
    year: [],
    status: [],
    comments: [],
    student: [],
  });

  constructor(
    protected busRouteService: BusRouteService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ busRoute }) => {
      this.updateForm(busRoute);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const busRoute = this.createFromForm();
    if (busRoute.id !== undefined) {
      this.subscribeToSaveResponse(this.busRouteService.update(busRoute));
    } else {
      this.subscribeToSaveResponse(this.busRouteService.create(busRoute));
    }
  }

  trackStudentById(index: number, item: IStudent): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusRoute>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(busRoute: IBusRoute): void {
    this.editForm.patchValue({
      id: busRoute.id,
      routeName: busRoute.routeName,
      routeDriver: busRoute.routeDriver,
      busNumber: busRoute.busNumber,
      year: busRoute.year,
      status: busRoute.status,
      comments: busRoute.comments,
      student: busRoute.student,
    });

    this.studentsSharedCollection = this.studentService.addStudentToCollectionIfMissing(this.studentsSharedCollection, busRoute.student);
  }

  protected loadRelationshipsOptions(): void {
    this.studentService
      .query()
      .pipe(map((res: HttpResponse<IStudent[]>) => res.body ?? []))
      .pipe(
        map((students: IStudent[]) => this.studentService.addStudentToCollectionIfMissing(students, this.editForm.get('student')!.value))
      )
      .subscribe((students: IStudent[]) => (this.studentsSharedCollection = students));
  }

  protected createFromForm(): IBusRoute {
    return {
      ...new BusRoute(),
      id: this.editForm.get(['id'])!.value,
      routeName: this.editForm.get(['routeName'])!.value,
      routeDriver: this.editForm.get(['routeDriver'])!.value,
      busNumber: this.editForm.get(['busNumber'])!.value,
      year: this.editForm.get(['year'])!.value,
      status: this.editForm.get(['status'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      student: this.editForm.get(['student'])!.value,
    };
  }
}
