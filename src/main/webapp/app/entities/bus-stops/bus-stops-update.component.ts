import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IBusStops, BusStops } from 'app/shared/model/bus-stops.model';
import { BusStopsService } from './bus-stops.service';
import { IBusRoute } from 'app/shared/model/bus-route.model';
import { BusRouteService } from 'app/entities/bus-route/bus-route.service';

@Component({
  selector: 'jhi-bus-stops-update',
  templateUrl: './bus-stops-update.component.html'
})
export class BusStopsUpdateComponent implements OnInit {
  isSaving = false;
  busroutes: IBusRoute[] = [];

  editForm = this.fb.group({
    id: [],
    routeName: [],
    busStops: [],
    busRoute: []
  });

  constructor(
    protected busStopsService: BusStopsService,
    protected busRouteService: BusRouteService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ busStops }) => {
      this.updateForm(busStops);

      this.busRouteService.query().subscribe((res: HttpResponse<IBusRoute[]>) => (this.busroutes = res.body || []));
    });
  }

  updateForm(busStops: IBusStops): void {
    this.editForm.patchValue({
      id: busStops.id,
      routeName: busStops.routeName,
      busStops: busStops.busStops,
      busRoute: busStops.busRoute
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const busStops = this.createFromForm();
    if (busStops.id !== undefined) {
      this.subscribeToSaveResponse(this.busStopsService.update(busStops));
    } else {
      this.subscribeToSaveResponse(this.busStopsService.create(busStops));
    }
  }

  private createFromForm(): IBusStops {
    return {
      ...new BusStops(),
      id: this.editForm.get(['id'])!.value,
      routeName: this.editForm.get(['routeName'])!.value,
      busStops: this.editForm.get(['busStops'])!.value,
      busRoute: this.editForm.get(['busRoute'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusStops>>): void {
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

  trackById(index: number, item: IBusRoute): any {
    return item.id;
  }
}
