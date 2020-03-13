import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'staff',
        loadChildren: () => import('./staff/staff.module').then(m => m.JhipsterSampleApplicationStaffModule)
      },
      {
        path: 'student',
        loadChildren: () => import('./student/student.module').then(m => m.JhipsterSampleApplicationStudentModule)
      },
      {
        path: 'class-name',
        loadChildren: () => import('./class-name/class-name.module').then(m => m.JhipsterSampleApplicationClassNameModule)
      },
      {
        path: 'section',
        loadChildren: () => import('./section/section.module').then(m => m.JhipsterSampleApplicationSectionModule)
      },
      {
        path: 'subject',
        loadChildren: () => import('./subject/subject.module').then(m => m.JhipsterSampleApplicationSubjectModule)
      },
      {
        path: 'student-markes',
        loadChildren: () => import('./student-markes/student-markes.module').then(m => m.JhipsterSampleApplicationStudentMarkesModule)
      },
      {
        path: 'attendence',
        loadChildren: () => import('./attendence/attendence.module').then(m => m.JhipsterSampleApplicationAttendenceModule)
      },
      {
        path: 'student-fee',
        loadChildren: () => import('./student-fee/student-fee.module').then(m => m.JhipsterSampleApplicationStudentFeeModule)
      },
      {
        path: 'bus-route',
        loadChildren: () => import('./bus-route/bus-route.module').then(m => m.JhipsterSampleApplicationBusRouteModule)
      },
      {
        path: 'bus-route-name',
        loadChildren: () => import('./bus-route-name/bus-route-name.module').then(m => m.JhipsterSampleApplicationBusRouteNameModule)
      },
      {
        path: 'bus-stops',
        loadChildren: () => import('./bus-stops/bus-stops.module').then(m => m.JhipsterSampleApplicationBusStopsModule)
      },
      {
        path: 'staff-salary',
        loadChildren: () => import('./staff-salary/staff-salary.module').then(m => m.JhipsterSampleApplicationStaffSalaryModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class JhipsterSampleApplicationEntityModule {}
