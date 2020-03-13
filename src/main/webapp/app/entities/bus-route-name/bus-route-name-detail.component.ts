import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBusRouteName } from 'app/shared/model/bus-route-name.model';

@Component({
  selector: 'jhi-bus-route-name-detail',
  templateUrl: './bus-route-name-detail.component.html'
})
export class BusRouteNameDetailComponent implements OnInit {
  busRouteName: IBusRouteName | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ busRouteName }) => (this.busRouteName = busRouteName));
  }

  previousState(): void {
    window.history.back();
  }
}
