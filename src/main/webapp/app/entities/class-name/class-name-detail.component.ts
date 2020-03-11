import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassName } from 'app/shared/model/class-name.model';

@Component({
  selector: 'jhi-class-name-detail',
  templateUrl: './class-name-detail.component.html'
})
export class ClassNameDetailComponent implements OnInit {
  className: IClassName | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ className }) => (this.className = className));
  }

  previousState(): void {
    window.history.back();
  }
}
