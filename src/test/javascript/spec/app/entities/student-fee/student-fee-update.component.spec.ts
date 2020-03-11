import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { StudentFeeUpdateComponent } from 'app/entities/student-fee/student-fee-update.component';
import { StudentFeeService } from 'app/entities/student-fee/student-fee.service';
import { StudentFee } from 'app/shared/model/student-fee.model';

describe('Component Tests', () => {
  describe('StudentFee Management Update Component', () => {
    let comp: StudentFeeUpdateComponent;
    let fixture: ComponentFixture<StudentFeeUpdateComponent>;
    let service: StudentFeeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [StudentFeeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(StudentFeeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentFeeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StudentFeeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new StudentFee(123);
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
        const entity = new StudentFee();
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
