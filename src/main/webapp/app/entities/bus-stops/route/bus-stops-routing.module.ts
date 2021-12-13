import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BusStopsComponent } from '../list/bus-stops.component';
import { BusStopsDetailComponent } from '../detail/bus-stops-detail.component';
import { BusStopsUpdateComponent } from '../update/bus-stops-update.component';
import { BusStopsRoutingResolveService } from './bus-stops-routing-resolve.service';

const busStopsRoute: Routes = [
  {
    path: '',
    component: BusStopsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BusStopsDetailComponent,
    resolve: {
      busStops: BusStopsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BusStopsUpdateComponent,
    resolve: {
      busStops: BusStopsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BusStopsUpdateComponent,
    resolve: {
      busStops: BusStopsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(busStopsRoute)],
  exports: [RouterModule],
})
export class BusStopsRoutingModule {}
