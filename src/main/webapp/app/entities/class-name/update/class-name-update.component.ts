import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClassName, ClassName } from '../class-name.model';
import { ClassNameService } from '../service/class-name.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { IStudentMarkes } from 'app/entities/student-markes/student-markes.model';
import { StudentMarkesService } from 'app/entities/student-markes/service/student-markes.service';
import { IStaff } from 'app/entities/staff/staff.model';
import { StaffService } from 'app/entities/staff/service/staff.service';

@Component({
  selector: 'jhi-class-name-update',
  templateUrl: './class-name-update.component.html',
})
export class ClassNameUpdateComponent implements OnInit {
  isSaving = false;

  studentsSharedCollection: IStudent[] = [];
  studentMarkesSharedCollection: IStudentMarkes[] = [];
  staffSharedCollection: IStaff[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    classNumber: [],
    student: [],
    studentMarkes: [],
    staff: [],
  });

  constructor(
    protected classNameService: ClassNameService,
    protected studentService: StudentService,
    protected studentMarkesService: StudentMarkesService,
    protected staffService: StaffService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ className }) => {
      this.updateForm(className);

      this.loadRelationshipsOptions();
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

  trackStudentById(index: number, item: IStudent): number {
    return item.id!;
  }

  trackStudentMarkesById(index: number, item: IStudentMarkes): number {
    return item.id!;
  }

  trackStaffById(index: number, item: IStaff): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassName>>): void {
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

  protected updateForm(className: IClassName): void {
    this.editForm.patchValue({
      id: className.id,
      name: className.name,
      classNumber: className.classNumber,
      student: className.student,
      studentMarkes: className.studentMarkes,
      staff: className.staff,
    });

    this.studentsSharedCollection = this.studentService.addStudentToCollectionIfMissing(this.studentsSharedCollection, className.student);
    this.studentMarkesSharedCollection = this.studentMarkesService.addStudentMarkesToCollectionIfMissing(
      this.studentMarkesSharedCollection,
      className.studentMarkes
    );
    this.staffSharedCollection = this.staffService.addStaffToCollectionIfMissing(this.staffSharedCollection, className.staff);
  }

  protected loadRelationshipsOptions(): void {
    this.studentService
      .query()
      .pipe(map((res: HttpResponse<IStudent[]>) => res.body ?? []))
      .pipe(
        map((students: IStudent[]) => this.studentService.addStudentToCollectionIfMissing(students, this.editForm.get('student')!.value))
      )
      .subscribe((students: IStudent[]) => (this.studentsSharedCollection = students));

    this.studentMarkesService
      .query()
      .pipe(map((res: HttpResponse<IStudentMarkes[]>) => res.body ?? []))
      .pipe(
        map((studentMarkes: IStudentMarkes[]) =>
          this.studentMarkesService.addStudentMarkesToCollectionIfMissing(studentMarkes, this.editForm.get('studentMarkes')!.value)
        )
      )
      .subscribe((studentMarkes: IStudentMarkes[]) => (this.studentMarkesSharedCollection = studentMarkes));

    this.staffService
      .query()
      .pipe(map((res: HttpResponse<IStaff[]>) => res.body ?? []))
      .pipe(map((staff: IStaff[]) => this.staffService.addStaffToCollectionIfMissing(staff, this.editForm.get('staff')!.value)))
      .subscribe((staff: IStaff[]) => (this.staffSharedCollection = staff));
  }

  protected createFromForm(): IClassName {
    return {
      ...new ClassName(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      classNumber: this.editForm.get(['classNumber'])!.value,
      student: this.editForm.get(['student'])!.value,
      studentMarkes: this.editForm.get(['studentMarkes'])!.value,
      staff: this.editForm.get(['staff'])!.value,
    };
  }
}
