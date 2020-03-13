import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAttendence, Attendence } from 'app/shared/model/attendence.model';
import { AttendenceService } from './attendence.service';
import { IStudent } from 'app/shared/model/student.model';
import { StudentService } from 'app/entities/student/student.service';

@Component({
  selector: 'jhi-attendence-update',
  templateUrl: './attendence-update.component.html'
})
export class AttendenceUpdateComponent implements OnInit {
  isSaving = false;
  students: IStudent[] = [];
  monthDp: any;

  editForm = this.fb.group({
    id: [],
    month: [],
    totalWorkingDays: [],
    dayspresent: [],
    student: []
  });

  constructor(
    protected attendenceService: AttendenceService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attendence }) => {
      this.updateForm(attendence);

      this.studentService.query().subscribe((res: HttpResponse<IStudent[]>) => (this.students = res.body || []));
    });
  }

  updateForm(attendence: IAttendence): void {
    this.editForm.patchValue({
      id: attendence.id,
      month: attendence.month,
      totalWorkingDays: attendence.totalWorkingDays,
      dayspresent: attendence.dayspresent,
      student: attendence.student
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

  private createFromForm(): IAttendence {
    return {
      ...new Attendence(),
      id: this.editForm.get(['id'])!.value,
      month: this.editForm.get(['month'])!.value,
      totalWorkingDays: this.editForm.get(['totalWorkingDays'])!.value,
      dayspresent: this.editForm.get(['dayspresent'])!.value,
      student: this.editForm.get(['student'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttendence>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IStudent): any {
    return item.id;
  }
}
