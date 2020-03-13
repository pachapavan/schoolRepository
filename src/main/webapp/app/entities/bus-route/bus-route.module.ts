import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationSharedModule } from 'app/shared/shared.module';
import { BusRouteComponent } from './bus-route.component';
import { BusRouteDetailComponent } from './bus-route-detail.component';
import { BusRouteUpdateComponent } from './bus-route-update.component';
import { BusRouteDeleteDialogComponent } from './bus-route-delete-dialog.component';
import { busRouteRoute } from './bus-route.route';

@NgModule({
  imports: [JhipsterSampleApplicationSharedModule, RouterModule.forChild(busRouteRoute)],
  declarations: [BusRouteComponent, BusRouteDetailComponent, BusRouteUpdateComponent, BusRouteDeleteDialogComponent],
  entryComponents: [BusRouteDeleteDialogComponent]
})
export class JhipsterSampleApplicationBusRouteModule {}
