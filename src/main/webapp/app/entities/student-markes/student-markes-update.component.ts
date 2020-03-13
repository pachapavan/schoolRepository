import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IStudentMarkes, StudentMarkes } from 'app/shared/model/student-markes.model';
import { StudentMarkesService } from './student-markes.service';
import { IStudent } from 'app/shared/model/student.model';
import { StudentService } from 'app/entities/student/student.service';

@Component({
  selector: 'jhi-student-markes-update',
  templateUrl: './student-markes-update.component.html'
})
export class StudentMarkesUpdateComponent implements OnInit {
  isSaving = false;
  students: IStudent[] = [];

  editForm = this.fb.group({
    id: [],
    examName: [],
    totalMarkes: [],
    markes: [],
    comments: [],
    student: []
  });

  constructor(
    protected studentMarkesService: StudentMarkesService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentMarkes }) => {
      this.updateForm(studentMarkes);

      this.studentService.query().subscribe((res: HttpResponse<IStudent[]>) => (this.students = res.body || []));
    });
  }

  updateForm(studentMarkes: IStudentMarkes): void {
    this.editForm.patchValue({
      id: studentMarkes.id,
      examName: studentMarkes.examName,
      totalMarkes: studentMarkes.totalMarkes,
      markes: studentMarkes.markes,
      comments: studentMarkes.comments,
      student: studentMarkes.student
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const studentMarkes = this.createFromForm();
    if (studentMarkes.id !== undefined) {
      this.subscribeToSaveResponse(this.studentMarkesService.update(studentMarkes));
    } else {
      this.subscribeToSaveResponse(this.studentMarkesService.create(studentMarkes));
    }
  }

  private createFromForm(): IStudentMarkes {
    return {
      ...new StudentMarkes(),
      id: this.editForm.get(['id'])!.value,
      examName: this.editForm.get(['examName'])!.value,
      totalMarkes: this.editForm.get(['totalMarkes'])!.value,
      markes: this.editForm.get(['markes'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      student: this.editForm.get(['student'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentMarkes>>): void {
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
