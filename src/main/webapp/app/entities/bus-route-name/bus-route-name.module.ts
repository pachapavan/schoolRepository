import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BusRouteNameComponent } from './list/bus-route-name.component';
import { BusRouteNameDetailComponent } from './detail/bus-route-name-detail.component';
import { BusRouteNameUpdateComponent } from './update/bus-route-name-update.component';
import { BusRouteNameDeleteDialogComponent } from './delete/bus-route-name-delete-dialog.component';
import { BusRouteNameRoutingModule } from './route/bus-route-name-routing.module';

@NgModule({
  imports: [SharedModule, BusRouteNameRoutingModule],
  declarations: [BusRouteNameComponent, BusRouteNameDetailComponent, BusRouteNameUpdateComponent, BusRouteNameDeleteDialogComponent],
  entryComponents: [BusRouteNameDeleteDialogComponent],
})
export class BusRouteNameModule {}
