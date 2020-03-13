import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IBusRouteName, BusRouteName } from 'app/shared/model/bus-route-name.model';
import { BusRouteNameService } from './bus-route-name.service';
import { IBusRoute } from 'app/shared/model/bus-route.model';
import { BusRouteService } from 'app/entities/bus-route/bus-route.service';

@Component({
  selector: 'jhi-bus-route-name-update',
  templateUrl: './bus-route-name-update.component.html'
})
export class BusRouteNameUpdateComponent implements OnInit {
  isSaving = false;
  busroutes: IBusRoute[] = [];

  editForm = this.fb.group({
    id: [],
    routeName: [],
    busRoute: []
  });

  constructor(
    protected busRouteNameService: BusRouteNameService,
    protected busRouteService: BusRouteService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ busRouteName }) => {
      this.updateForm(busRouteName);

      this.busRouteService.query().subscribe((res: HttpResponse<IBusRoute[]>) => (this.busroutes = res.body || []));
    });
  }

  updateForm(busRouteName: IBusRouteName): void {
    this.editForm.patchValue({
      id: busRouteName.id,
      routeName: busRouteName.routeName,
      busRoute: busRouteName.busRoute
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

  private createFromForm(): IBusRouteName {
    return {
      ...new BusRouteName(),
      id: this.editForm.get(['id'])!.value,
      routeName: this.editForm.get(['routeName'])!.value,
      busRoute: this.editForm.get(['busRoute'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusRouteName>>): void {
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
