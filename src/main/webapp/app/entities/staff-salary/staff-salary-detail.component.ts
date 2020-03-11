import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStaffSalary } from 'app/shared/model/staff-salary.model';

@Component({
  selector: 'jhi-staff-salary-detail',
  templateUrl: './staff-salary-detail.component.html'
})
export class StaffSalaryDetailComponent implements OnInit {
  staffSalary: IStaffSalary | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ staffSalary }) => (this.staffSalary = staffSalary));
  }

  previousState(): void {
    window.history.back();
  }
}
