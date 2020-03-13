import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationSharedModule } from 'app/shared/shared.module';
import { BusStopsComponent } from './bus-stops.component';
import { BusStopsDetailComponent } from './bus-stops-detail.component';
import { BusStopsUpdateComponent } from './bus-stops-update.component';
import { BusStopsDeleteDialogComponent } from './bus-stops-delete-dialog.component';
import { busStopsRoute } from './bus-stops.route';

@NgModule({
  imports: [JhipsterSampleApplicationSharedModule, RouterModule.forChild(busStopsRoute)],
  declarations: [BusStopsComponent, BusStopsDetailComponent, BusStopsUpdateComponent, BusStopsDeleteDialogComponent],
  entryComponents: [BusStopsDeleteDialogComponent]
})
export class JhipsterSampleApplicationBusStopsModule {}
