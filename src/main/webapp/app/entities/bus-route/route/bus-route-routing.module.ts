import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BusRouteComponent } from '../list/bus-route.component';
import { BusRouteDetailComponent } from '../detail/bus-route-detail.component';
import { BusRouteUpdateComponent } from '../update/bus-route-update.component';
import { BusRouteRoutingResolveService } from './bus-route-routing-resolve.service';

const busRouteRoute: Routes = [
  {
    path: '',
    component: BusRouteComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BusRouteDetailComponent,
    resolve: {
      busRoute: BusRouteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BusRouteUpdateComponent,
    resolve: {
      busRoute: BusRouteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BusRouteUpdateComponent,
    resolve: {
      busRoute: BusRouteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(busRouteRoute)],
  exports: [RouterModule],
})
export class BusRouteRoutingModule {}
