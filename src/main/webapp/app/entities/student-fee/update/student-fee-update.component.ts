import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStudentFee, StudentFee } from '../student-fee.model';
import { StudentFeeService } from '../service/student-fee.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

@Component({
  selector: 'jhi-student-fee-update',
  templateUrl: './student-fee-update.component.html',
})
export class StudentFeeUpdateComponent implements OnInit {
  isSaving = false;

  studentsSharedCollection: IStudent[] = [];

  editForm = this.fb.group({
    id: [],
    totalAcademicFee: [],
    academicFeewaveOff: [],
    academicFeePaid: [],
    totalAcademicFeePaid: [],
    academicFeepending: [],
    busAlloted: [],
    hostelAlloted: [],
    totalBusFee: [],
    busFeewaveOff: [],
    busFeePaid: [],
    totalBusFeePaid: [],
    busFeepending: [],
    totalHostelFee: [],
    hostelFeewaveOff: [],
    totalHostelFeePaid: [],
    hostelFeePaid: [],
    hostelFeepending: [],
    hostelExpenses: [],
    year: [],
    comments: [],
    student: [],
  });

  constructor(
    protected studentFeeService: StudentFeeService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentFee }) => {
      this.updateForm(studentFee);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const studentFee = this.createFromForm();
    if (studentFee.id !== undefined) {
      this.subscribeToSaveResponse(this.studentFeeService.update(studentFee));
    } else {
      this.subscribeToSaveResponse(this.studentFeeService.create(studentFee));
    }
  }

  trackStudentById(index: number, item: IStudent): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentFee>>): void {
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

  protected updateForm(studentFee: IStudentFee): void {
    this.editForm.patchValue({
      id: studentFee.id,
      totalAcademicFee: studentFee.totalAcademicFee,
      academicFeewaveOff: studentFee.academicFeewaveOff,
      academicFeePaid: studentFee.academicFeePaid,
      totalAcademicFeePaid: studentFee.totalAcademicFeePaid,
      academicFeepending: studentFee.academicFeepending,
      busAlloted: studentFee.busAlloted,
      hostelAlloted: studentFee.hostelAlloted,
      totalBusFee: studentFee.totalBusFee,
      busFeewaveOff: studentFee.busFeewaveOff,
      busFeePaid: studentFee.busFeePaid,
      totalBusFeePaid: studentFee.totalBusFeePaid,
      busFeepending: studentFee.busFeepending,
      totalHostelFee: studentFee.totalHostelFee,
      hostelFeewaveOff: studentFee.hostelFeewaveOff,
      totalHostelFeePaid: studentFee.totalHostelFeePaid,
      hostelFeePaid: studentFee.hostelFeePaid,
      hostelFeepending: studentFee.hostelFeepending,
      hostelExpenses: studentFee.hostelExpenses,
      year: studentFee.year,
      comments: studentFee.comments,
      student: studentFee.student,
    });

    this.studentsSharedCollection = this.studentService.addStudentToCollectionIfMissing(this.studentsSharedCollection, studentFee.student);
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

  protected createFromForm(): IStudentFee {
    return {
      ...new StudentFee(),
      id: this.editForm.get(['id'])!.value,
      totalAcademicFee: this.editForm.get(['totalAcademicFee'])!.value,
      academicFeewaveOff: this.editForm.get(['academicFeewaveOff'])!.value,
      academicFeePaid: this.editForm.get(['academicFeePaid'])!.value,
      totalAcademicFeePaid: this.editForm.get(['totalAcademicFeePaid'])!.value,
      academicFeepending: this.editForm.get(['academicFeepending'])!.value,
      busAlloted: this.editForm.get(['busAlloted'])!.value,
      hostelAlloted: this.editForm.get(['hostelAlloted'])!.value,
      totalBusFee: this.editForm.get(['totalBusFee'])!.value,
      busFeewaveOff: this.editForm.get(['busFeewaveOff'])!.value,
      busFeePaid: this.editForm.get(['busFeePaid'])!.value,
      totalBusFeePaid: this.editForm.get(['totalBusFeePaid'])!.value,
      busFeepending: this.editForm.get(['busFeepending'])!.value,
      totalHostelFee: this.editForm.get(['totalHostelFee'])!.value,
      hostelFeewaveOff: this.editForm.get(['hostelFeewaveOff'])!.value,
      totalHostelFeePaid: this.editForm.get(['totalHostelFeePaid'])!.value,
      hostelFeePaid: this.editForm.get(['hostelFeePaid'])!.value,
      hostelFeepending: this.editForm.get(['hostelFeepending'])!.value,
      hostelExpenses: this.editForm.get(['hostelExpenses'])!.value,
      year: this.editForm.get(['year'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      student: this.editForm.get(['student'])!.value,
    };
  }
}
