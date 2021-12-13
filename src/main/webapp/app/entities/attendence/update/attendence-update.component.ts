import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAttendence, Attendence } from '../attendence.model';
import { AttendenceService } from '../service/attendence.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

@Component({
  selector: 'jhi-attendence-update',
  templateUrl: './attendence-update.component.html',
})
export class AttendenceUpdateComponent implements OnInit {
  isSaving = false;

  studentsSharedCollection: IStudent[] = [];

  editForm = this.fb.group({
    id: [],
    month: [],
    totalWorkingDays: [],
    dayspresent: [],
    student: [],
  });

  constructor(
    protected attendenceService: AttendenceService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attendence }) => {
      this.updateForm(attendence);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attendence = this.createFromForm();
    if (attendence.id !== undefined) {
      this.subscribeToSaveResponse(this.attendenceService.update(attendence));
    } else {
      this.subscribeToSaveResponse(this.attendenceService.create(attendence));
    }
  }

  trackStudentById(index: number, item: IStudent): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttendence>>): void {
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

  protected updateForm(attendence: IAttendence): void {
    this.editForm.patchValue({
      id: attendence.id,
      month: attendence.month,
      totalWorkingDays: attendence.totalWorkingDays,
      dayspresent: attendence.dayspresent,
      student: attendence.student,
    });

    this.studentsSharedCollection = this.studentService.addStudentToCollectionIfMissing(this.studentsSharedCollection, attendence.student);
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

  protected createFromForm(): IAttendence {
    return {
      ...new Attendence(),
      id: this.editForm.get(['id'])!.value,
      month: this.editForm.get(['month'])!.value,
      totalWorkingDays: this.editForm.get(['totalWorkingDays'])!.value,
      dayspresent: this.editForm.get(['dayspresent'])!.value,
      student: this.editForm.get(['student'])!.value,
    };
  }
}
