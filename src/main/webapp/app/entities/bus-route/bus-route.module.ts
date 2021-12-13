import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BusRouteComponent } from './list/bus-route.component';
import { BusRouteDetailComponent } from './detail/bus-route-detail.component';
import { BusRouteUpdateComponent } from './update/bus-route-update.component';
import { BusRouteDeleteDialogComponent } from './delete/bus-route-delete-dialog.component';
import { BusRouteRoutingModule } from './route/bus-route-routing.module';

@NgModule({
  imports: [SharedModule, BusRouteRoutingModule],
  declarations: [BusRouteComponent, BusRouteDetailComponent, BusRouteUpdateComponent, BusRouteDeleteDialogComponent],
  entryComponents: [BusRouteDeleteDialogComponent],
})
export class BusRouteModule {}
