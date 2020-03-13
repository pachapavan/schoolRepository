import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationSharedModule } from 'app/shared/shared.module';
import { BusRouteNameComponent } from './bus-route-name.component';
import { BusRouteNameDetailComponent } from './bus-route-name-detail.component';
import { BusRouteNameUpdateComponent } from './bus-route-name-update.component';
import { BusRouteNameDeleteDialogComponent } from './bus-route-name-delete-dialog.component';
import { busRouteNameRoute } from './bus-route-name.route';

@NgModule({
  imports: [JhipsterSampleApplicationSharedModule, RouterModule.forChild(busRouteNameRoute)],
  declarations: [BusRouteNameComponent, BusRouteNameDetailComponent, BusRouteNameUpdateComponent, BusRouteNameDeleteDialogComponent],
  entryComponents: [BusRouteNameDeleteDialogComponent]
})
export class JhipsterSampleApplicationBusRouteNameModule {}
