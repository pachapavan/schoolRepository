import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClassNameComponent } from '../list/class-name.component';
import { ClassNameDetailComponent } from '../detail/class-name-detail.component';
import { ClassNameUpdateComponent } from '../update/class-name-update.component';
import { ClassNameRoutingResolveService } from './class-name-routing-resolve.service';

const classNameRoute: Routes = [
  {
    path: '',
    component: ClassNameComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassNameDetailComponent,
    resolve: {
      className: ClassNameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassNameUpdateComponent,
    resolve: {
      className: ClassNameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassNameUpdateComponent,
    resolve: {
      className: ClassNameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(classNameRoute)],
  exports: [RouterModule],
})
export class ClassNameRoutingModule {}
