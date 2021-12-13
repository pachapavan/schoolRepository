import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBusRouteName, BusRouteName } from '../bus-route-name.model';
import { BusRouteNameService } from '../service/bus-route-name.service';
import { IBusRoute } from 'app/entities/bus-route/bus-route.model';
import { BusRouteService } from 'app/entities/bus-route/service/bus-route.service';

@Component({
  selector: 'jhi-bus-route-name-update',
  templateUrl: './bus-route-name-update.component.html',
})
export class BusRouteNameUpdateComponent implements OnInit {
  isSaving = false;

  busRoutesSharedCollection: IBusRoute[] = [];

  editForm = this.fb.group({
    id: [],
    routeName: [],
    busRoute: [],
  });

  constructor(
    protected busRouteNameService: BusRouteNameService,
    protected busRouteService: BusRouteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ busRouteName }) => {
      this.updateForm(busRouteName);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const busRouteName = this.createFromForm();
    if (busRouteName.id !== undefined) {
      this.subscribeToSaveResponse(this.busRouteNameService.update(busRouteName));
    } else {
      this.subscribeToSaveResponse(this.busRouteNameService.create(busRouteName));
    }
  }

  trackBusRouteById(index: number, item: IBusRoute): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusRouteName>>): void {
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

  protected updateForm(busRouteName: IBusRouteName): void {
    this.editForm.patchValue({
      id: busRouteName.id,
      routeName: busRouteName.routeName,
      busRoute: busRouteName.busRoute,
    });

    this.busRoutesSharedCollection = this.busRouteService.addBusRouteToCollectionIfMissing(
      this.busRoutesSharedCollection,
      busRouteName.busRoute
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

  protected createFromForm(): IBusRouteName {
    return {
      ...new BusRouteName(),
      id: this.editForm.get(['id'])!.value,
      routeName: this.editForm.get(['routeName'])!.value,
      busRoute: this.editForm.get(['busRoute'])!.value,
    };
  }
}
