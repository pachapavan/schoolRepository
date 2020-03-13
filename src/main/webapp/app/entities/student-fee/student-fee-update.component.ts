import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IStudentFee, StudentFee } from 'app/shared/model/student-fee.model';
import { StudentFeeService } from './student-fee.service';
import { IStudent } from 'app/shared/model/student.model';
import { StudentService } from 'app/entities/student/student.service';

@Component({
  selector: 'jhi-student-fee-update',
  templateUrl: './student-fee-update.component.html'
})
export class StudentFeeUpdateComponent implements OnInit {
  isSaving = false;
  students: IStudent[] = [];

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
    student: []
  });

  constructor(
    protected studentFeeService: StudentFeeService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentFee }) => {
      this.updateForm(studentFee);

      this.studentService.query().subscribe((res: HttpResponse<IStudent[]>) => (this.students = res.body || []));
    });
  }

  updateForm(studentFee: IStudentFee): void {
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
      student: studentFee.student
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

  private createFromForm(): IStudentFee {
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
      student: this.editForm.get(['student'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentFee>>): void {
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
