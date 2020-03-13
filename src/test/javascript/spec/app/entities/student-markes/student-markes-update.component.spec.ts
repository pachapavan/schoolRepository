import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { StudentMarkesUpdateComponent } from 'app/entities/student-markes/student-markes-update.component';
import { StudentMarkesService } from 'app/entities/student-markes/student-markes.service';
import { StudentMarkes } from 'app/shared/model/student-markes.model';

describe('Component Tests', () => {
  describe('StudentMarkes Management Update Component', () => {
    let comp: StudentMarkesUpdateComponent;
    let fixture: ComponentFixture<StudentMarkesUpdateComponent>;
    let service: StudentMarkesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [StudentMarkesUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(StudentMarkesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentMarkesUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StudentMarkesService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new StudentMarkes(123);
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
        const entity = new StudentMarkes();
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
