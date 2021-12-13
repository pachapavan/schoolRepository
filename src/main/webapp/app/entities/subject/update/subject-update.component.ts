import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISubject, Subject } from '../subject.model';
import { SubjectService } from '../service/subject.service';
import { IStudentMarkes } from 'app/entities/student-markes/student-markes.model';
import { StudentMarkesService } from 'app/entities/student-markes/service/student-markes.service';

@Component({
  selector: 'jhi-subject-update',
  templateUrl: './subject-update.component.html',
})
export class SubjectUpdateComponent implements OnInit {
  isSaving = false;

  studentMarkesSharedCollection: IStudentMarkes[] = [];

  editForm = this.fb.group({
    id: [],
    classname: [],
    section: [],
    subjectName: [],
    subjectCode: [],
    subjectTeacher: [],
    studentMarkes: [],
  });

  constructor(
    protected subjectService: SubjectService,
    protected studentMarkesService: StudentMarkesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subject }) => {
      this.updateForm(subject);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subject = this.createFromForm();
    if (subject.id !== undefined) {
      this.subscribeToSaveResponse(this.subjectService.update(subject));
    } else {
      this.subscribeToSaveResponse(this.subjectService.create(subject));
    }
  }

  trackStudentMarkesById(index: number, item: IStudentMarkes): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubject>>): void {
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

  protected updateForm(subject: ISubject): void {
    this.editForm.patchValue({
      id: subject.id,
      classname: subject.classname,
      section: subject.section,
      subjectName: subject.subjectName,
      subjectCode: subject.subjectCode,
      subjectTeacher: subject.subjectTeacher,
      studentMarkes: subject.studentMarkes,
    });

    this.studentMarkesSharedCollection = this.studentMarkesService.addStudentMarkesToCollectionIfMissing(
      this.studentMarkesSharedCollection,
      subject.studentMarkes
    );
  }

  protected loadRelationshipsOptions(): void {
    this.studentMarkesService
      .query()
      .pipe(map((res: HttpResponse<IStudentMarkes[]>) => res.body ?? []))
      .pipe(
        map((studentMarkes: IStudentMarkes[]) =>
          this.studentMarkesService.addStudentMarkesToCollectionIfMissing(studentMarkes, this.editForm.get('studentMarkes')!.value)
        )
      )
      .subscribe((studentMarkes: IStudentMarkes[]) => (this.studentMarkesSharedCollection = studentMarkes));
  }

  protected createFromForm(): ISubject {
    return {
      ...new Subject(),
      id: this.editForm.get(['id'])!.value,
      classname: this.editForm.get(['classname'])!.value,
      section: this.editForm.get(['section'])!.value,
      subjectName: this.editForm.get(['subjectName'])!.value,
      subjectCode: this.editForm.get(['subjectCode'])!.value,
      subjectTeacher: this.editForm.get(['subjectTeacher'])!.value,
      studentMarkes: this.editForm.get(['studentMarkes'])!.value,
    };
  }
}
