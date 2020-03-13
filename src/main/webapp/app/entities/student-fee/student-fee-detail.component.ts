import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudentFee } from 'app/shared/model/student-fee.model';

@Component({
  selector: 'jhi-student-fee-detail',
  templateUrl: './student-fee-detail.component.html'
})
export class StudentFeeDetailComponent implements OnInit {
  studentFee: IStudentFee | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentFee }) => (this.studentFee = studentFee));
  }

  previousState(): void {
    window.history.back();
  }
}
