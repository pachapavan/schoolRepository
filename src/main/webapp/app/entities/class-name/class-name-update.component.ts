import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IClassName, ClassName } from 'app/shared/model/class-name.model';
import { ClassNameService } from './class-name.service';
import { IStudent } from 'app/shared/model/student.model';
import { StudentService } from 'app/entities/student/student.service';
import { IStudentMarkes } from 'app/shared/model/student-markes.model';
import { StudentMarkesService } from 'app/entities/student-markes/student-markes.service';
import { IStaff } from 'app/shared/model/staff.model';
import { StaffService } from 'app/entities/staff/staff.service';

type SelectableEntity = IStudent | IStudentMarkes | IStaff;

@Component({
  selector: 'jhi-class-name-update',
  templateUrl: './class-name-update.component.html'
})
export class ClassNameUpdateComponent implements OnInit {
  isSaving = false;
  students: IStudent[] = [];
  studentmarkes: IStudentMarkes[] = [];
  staff: IStaff[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    classNumber: [],
    student: [],
    studentMarkes: [],
    staff: []
  });

  constructor(
    protected classNameService: ClassNameService,
    protected studentService: StudentService,
    protected studentMarkesService: StudentMarkesService,
    protected staffService: StaffService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ className }) => {
      this.updateForm(className);

      this.studentService.query().subscribe((res: HttpResponse<IStudent[]>) => (this.students = res.body || []));

      this.studentMarkesService.query().subscribe((res: HttpResponse<IStudentMarkes[]>) => (this.studentmarkes = res.body || []));

      this.staffService.query().subscribe((res: HttpResponse<IStaff[]>) => (this.staff = res.body || []));
    });
  }

  updateForm(className: IClassName): void {
    this.editForm.patchValue({
      id: className.id,
      name: className.name,
      classNumber: className.classNumber,
      student: className.student,
      studentMarkes: className.studentMarkes,
      staff: className.staff
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const className = this.createFromForm();
    if (className.id !== undefined) {
      this.subscribeToSaveResponse(this.classNameService.update(className));
    } else {
      this.subscribeToSaveResponse(this.classNameService.create(className));
    }
  }

  private createFromForm(): IClassName {
    return {
      ...new ClassName(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      classNumber: this.editForm.get(['classNumber'])!.value,
      student: this.editForm.get(['student'])!.value,
      studentMarkes: this.editForm.get(['studentMarkes'])!.value,
      staff: this.editForm.get(['staff'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassName>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
