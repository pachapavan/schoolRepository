import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IStaffSalary, StaffSalary } from 'app/shared/model/staff-salary.model';
import { StaffSalaryService } from './staff-salary.service';
import { IStaff } from 'app/shared/model/staff.model';
import { StaffService } from 'app/entities/staff/staff.service';

@Component({
  selector: 'jhi-staff-salary-update',
  templateUrl: './staff-salary-update.component.html'
})
export class StaffSalaryUpdateComponent implements OnInit {
  isSaving = false;
  staff: IStaff[] = [];

  editForm = this.fb.group({
    id: [],
    salaryPaid: [],
    month: [],
    staff: []
  });

  constructor(
    protected staffSalaryService: StaffSalaryService,
    protected staffService: StaffService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ staffSalary }) => {
      this.updateForm(staffSalary);

      this.staffService.query().subscribe((res: HttpResponse<IStaff[]>) => (this.staff = res.body || []));
    });
  }

  updateForm(staffSalary: IStaffSalary): void {
    this.editForm.patchValue({
      id: staffSalary.id,
      salaryPaid: staffSalary.salaryPaid,
      month: staffSalary.month,
      staff: staffSalary.staff
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const staffSalary = this.createFromForm();
    if (staffSalary.id !== undefined) {
      this.subscribeToSaveResponse(this.staffSalaryService.update(staffSalary));
    } else {
      this.subscribeToSaveResponse(this.staffSalaryService.create(staffSalary));
    }
  }

  private createFromForm(): IStaffSalary {
    return {
      ...new StaffSalary(),
      id: this.editForm.get(['id'])!.value,
      salaryPaid: this.editForm.get(['salaryPaid'])!.value,
      month: this.editForm.get(['month'])!.value,
      staff: this.editForm.get(['staff'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStaffSalary>>): void {
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

  trackById(index: number, item: IStaff): any {
    return item.id;
  }
}
