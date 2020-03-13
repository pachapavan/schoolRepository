import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationSharedModule } from 'app/shared/shared.module';
import { ClassNameComponent } from './class-name.component';
import { ClassNameDetailComponent } from './class-name-detail.component';
import { ClassNameUpdateComponent } from './class-name-update.component';
import { ClassNameDeleteDialogComponent } from './class-name-delete-dialog.component';
import { classNameRoute } from './class-name.route';

@NgModule({
  imports: [JhipsterSampleApplicationSharedModule, RouterModule.forChild(classNameRoute)],
  declarations: [ClassNameComponent, ClassNameDetailComponent, ClassNameUpdateComponent, ClassNameDeleteDialogComponent],
  entryComponents: [ClassNameDeleteDialogComponent]
})
export class JhipsterSampleApplicationClassNameModule {}
