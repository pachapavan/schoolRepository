import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BusStopsComponent } from './list/bus-stops.component';
import { BusStopsDetailComponent } from './detail/bus-stops-detail.component';
import { BusStopsUpdateComponent } from './update/bus-stops-update.component';
import { BusStopsDeleteDialogComponent } from './delete/bus-stops-delete-dialog.component';
import { BusStopsRoutingModule } from './route/bus-stops-routing.module';

@NgModule({
  imports: [SharedModule, BusStopsRoutingModule],
  declarations: [BusStopsComponent, BusStopsDetailComponent, BusStopsUpdateComponent, BusStopsDeleteDialogComponent],
  entryComponents: [BusStopsDeleteDialogComponent],
})
export class BusStopsModule {}
