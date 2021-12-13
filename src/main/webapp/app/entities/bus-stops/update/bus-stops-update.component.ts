import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBusStops, BusStops } from '../bus-stops.model';
import { BusStopsService } from '../service/bus-stops.service';
import { IBusRoute } from 'app/entities/bus-route/bus-route.model';
import { BusRouteService } from 'app/entities/bus-route/service/bus-route.service';

@Component({
  selector: 'jhi-bus-stops-update',
  templateUrl: './bus-stops-update.component.html',
})
export class BusStopsUpdateComponent implements OnInit {
  isSaving = false;

  busRoutesSharedCollection: IBusRoute[] = [];

  editForm = this.fb.group({
    id: [],
    routeName: [],
    busStops: [],
    busRoute: [],
  });

  constructor(
    protected busStopsService: BusStopsService,
    protected busRouteService: BusRouteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ busStops }) => {
      this.updateForm(busStops);

      this.loadRelationshipsOptions();
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

  trackBusRouteById(index: number, item: IBusRoute): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusStops>>): void {
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

  protected updateForm(busStops: IBusStops): void {
    this.editForm.patchValue({
      id: busStops.id,
      routeName: busStops.routeName,
      busStops: busStops.busStops,
      busRoute: busStops.busRoute,
    });

    this.busRoutesSharedCollection = this.busRouteService.addBusRouteToCollectionIfMissing(
      this.busRoutesSharedCollection,
      busStops.busRoute
    );
  }

  protected loadRelationshipsOptions(): void {
    this.busRouteService
      .query()
      .pipe(map((res: HttpResponse<IBusRoute[]>) => res.body ?? []))
      .pipe(
        map((busRoutes: IBusRoute[]) =>
          this.busRouteService.addBusRouteToCollectionIfMissing(busRoutes, this.editForm.get('busRoute')!.value)
        )
      )
      .subscribe((busRoutes: IBusRoute[]) => (this.busRoutesSharedCollection = busRoutes));
  }

  protected createFromForm(): IBusStops {
    return {
      ...new BusStops(),
      id: this.editForm.get(['id'])!.value,
      routeName: this.editForm.get(['routeName'])!.value,
      busStops: this.editForm.get(['busStops'])!.value,
      busRoute: this.editForm.get(['busRoute'])!.value,
    };
  }
}
