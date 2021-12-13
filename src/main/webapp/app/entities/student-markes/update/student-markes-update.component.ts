import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStudentMarkes, StudentMarkes } from '../student-markes.model';
import { StudentMarkesService } from '../service/student-markes.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

@Component({
  selector: 'jhi-student-markes-update',
  templateUrl: './student-markes-update.component.html',
})
export class StudentMarkesUpdateComponent implements OnInit {
  isSaving = false;

  studentsSharedCollection: IStudent[] = [];

  editForm = this.fb.group({
    id: [],
    examName: [],
    totalMarkes: [],
    markes: [],
    comments: [],
    student: [],
  });

  constructor(
    protected studentMarkesService: StudentMarkesService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentMarkes }) => {
      this.updateForm(studentMarkes);

      this.loadRelationshipsOptions();
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

  trackStudentById(index: number, item: IStudent): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentMarkes>>): void {
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

  protected updateForm(studentMarkes: IStudentMarkes): void {
    this.editForm.patchValue({
      id: studentMarkes.id,
      examName: studentMarkes.examName,
      totalMarkes: studentMarkes.totalMarkes,
      markes: studentMarkes.markes,
      comments: studentMarkes.comments,
      student: studentMarkes.student,
    });

    this.studentsSharedCollection = this.studentService.addStudentToCollectionIfMissing(
      this.studentsSharedCollection,
      studentMarkes.student
    );
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

  protected createFromForm(): IStudentMarkes {
    return {
      ...new StudentMarkes(),
      id: this.editForm.get(['id'])!.value,
      examName: this.editForm.get(['examName'])!.value,
      totalMarkes: this.editForm.get(['totalMarkes'])!.value,
      markes: this.editForm.get(['markes'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      student: this.editForm.get(['student'])!.value,
    };
  }
}
