import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BusRouteNameComponent } from '../list/bus-route-name.component';
import { BusRouteNameDetailComponent } from '../detail/bus-route-name-detail.component';
import { BusRouteNameUpdateComponent } from '../update/bus-route-name-update.component';
import { BusRouteNameRoutingResolveService } from './bus-route-name-routing-resolve.service';

const busRouteNameRoute: Routes = [
  {
    path: '',
    component: BusRouteNameComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BusRouteNameDetailComponent,
    resolve: {
      busRouteName: BusRouteNameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BusRouteNameUpdateComponent,
    resolve: {
      busRouteName: BusRouteNameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BusRouteNameUpdateComponent,
    resolve: {
      busRouteName: BusRouteNameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(busRouteNameRoute)],
  exports: [RouterModule],
})
export class BusRouteNameRoutingModule {}
