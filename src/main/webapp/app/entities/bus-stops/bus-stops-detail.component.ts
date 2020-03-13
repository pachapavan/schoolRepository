import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBusStops } from 'app/shared/model/bus-stops.model';

@Component({
  selector: 'jhi-bus-stops-detail',
  templateUrl: './bus-stops-detail.component.html'
})
export class BusStopsDetailComponent implements OnInit {
  busStops: IBusStops | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ busStops }) => (this.busStops = busStops));
  }

  previousState(): void {
    window.history.back();
  }
}
