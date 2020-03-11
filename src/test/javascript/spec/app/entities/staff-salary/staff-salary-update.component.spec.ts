import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { StaffSalaryUpdateComponent } from 'app/entities/staff-salary/staff-salary-update.component';
import { StaffSalaryService } from 'app/entities/staff-salary/staff-salary.service';
import { StaffSalary } from 'app/shared/model/staff-salary.model';

describe('Component Tests', () => {
  describe('StaffSalary Management Update Component', () => {
    let comp: StaffSalaryUpdateComponent;
    let fixture: ComponentFixture<StaffSalaryUpdateComponent>;
    let service: StaffSalaryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [StaffSalaryUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(StaffSalaryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StaffSalaryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StaffSalaryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new StaffSalary(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new StaffSalary();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
