import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBusRoute } from 'app/shared/model/bus-route.model';

@Component({
  selector: 'jhi-bus-route-detail',
  templateUrl: './bus-route-detail.component.html'
})
export class BusRouteDetailComponent implements OnInit {
  busRoute: IBusRoute | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ busRoute }) => (this.busRoute = busRoute));
  }

  previousState(): void {
    window.history.back();
  }
}
