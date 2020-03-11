import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISubject, Subject } from 'app/shared/model/subject.model';
import { SubjectService } from './subject.service';
import { IStudentMarkes } from 'app/shared/model/student-markes.model';
import { StudentMarkesService } from 'app/entities/student-markes/student-markes.service';

@Component({
  selector: 'jhi-subject-update',
  templateUrl: './subject-update.component.html'
})
export class SubjectUpdateComponent implements OnInit {
  isSaving = false;
  studentmarkes: IStudentMarkes[] = [];

  editForm = this.fb.group({
    id: [],
    classname: [],
    section: [],
    subjectName: [],
    subjectCode: [],
    subjectTeacher: [],
    studentMarkes: []
  });

  constructor(
    protected subjectService: SubjectService,
    protected studentMarkesService: StudentMarkesService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subject }) => {
      this.updateForm(subject);

      this.studentMarkesService.query().subscribe((res: HttpResponse<IStudentMarkes[]>) => (this.studentmarkes = res.body || []));
    });
  }

  updateForm(subject: ISubject): void {
    this.editForm.patchValue({
      id: subject.id,
      classname: subject.classname,
      section: subject.section,
      subjectName: subject.subjectName,
      subjectCode: subject.subjectCode,
      subjectTeacher: subject.subjectTeacher,
      studentMarkes: subject.studentMarkes
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

  private createFromForm(): ISubject {
    return {
      ...new Subject(),
      id: this.editForm.get(['id'])!.value,
      classname: this.editForm.get(['classname'])!.value,
      section: this.editForm.get(['section'])!.value,
      subjectName: this.editForm.get(['subjectName'])!.value,
      subjectCode: this.editForm.get(['subjectCode'])!.value,
      subjectTeacher: this.editForm.get(['subjectTeacher'])!.value,
      studentMarkes: this.editForm.get(['studentMarkes'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubject>>): void {
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

  trackById(index: number, item: IStudentMarkes): any {
    return item.id;
  }
}
