import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStaffSalary, StaffSalary } from '../staff-salary.model';
import { StaffSalaryService } from '../service/staff-salary.service';
import { IStaff } from 'app/entities/staff/staff.model';
import { StaffService } from 'app/entities/staff/service/staff.service';

@Component({
  selector: 'jhi-staff-salary-update',
  templateUrl: './staff-salary-update.component.html',
})
export class StaffSalaryUpdateComponent implements OnInit {
  isSaving = false;

  staffSharedCollection: IStaff[] = [];

  editForm = this.fb.group({
    id: [],
    salaryPaid: [],
    month: [],
    staff: [],
  });

  constructor(
    protected staffSalaryService: StaffSalaryService,
    protected staffService: StaffService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ staffSalary }) => {
      this.updateForm(staffSalary);

      this.loadRelationshipsOptions();
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

  trackStaffById(index: number, item: IStaff): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStaffSalary>>): void {
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

  protected updateForm(staffSalary: IStaffSalary): void {
    this.editForm.patchValue({
      id: staffSalary.id,
      salaryPaid: staffSalary.salaryPaid,
      month: staffSalary.month,
      staff: staffSalary.staff,
    });

    this.staffSharedCollection = this.staffService.addStaffToCollectionIfMissing(this.staffSharedCollection, staffSalary.staff);
  }

  protected loadRelationshipsOptions(): void {
    this.staffService
      .query()
      .pipe(map((res: HttpResponse<IStaff[]>) => res.body ?? []))
      .pipe(map((staff: IStaff[]) => this.staffService.addStaffToCollectionIfMissing(staff, this.editForm.get('staff')!.value)))
      .subscribe((staff: IStaff[]) => (this.staffSharedCollection = staff));
  }

  protected createFromForm(): IStaffSalary {
    return {
      ...new StaffSalary(),
      id: this.editForm.get(['id'])!.value,
      salaryPaid: this.editForm.get(['salaryPaid'])!.value,
      month: this.editForm.get(['month'])!.value,
      staff: this.editForm.get(['staff'])!.value,
    };
  }
}
