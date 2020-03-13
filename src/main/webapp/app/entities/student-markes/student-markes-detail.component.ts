import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudentMarkes } from 'app/shared/model/student-markes.model';

@Component({
  selector: 'jhi-student-markes-detail',
  templateUrl: './student-markes-detail.component.html'
})
export class StudentMarkesDetailComponent implements OnInit {
  studentMarkes: IStudentMarkes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentMarkes }) => (this.studentMarkes = studentMarkes));
  }

  previousState(): void {
    window.history.back();
  }
}
