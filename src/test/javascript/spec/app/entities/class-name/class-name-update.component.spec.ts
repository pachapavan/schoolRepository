import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { ClassNameUpdateComponent } from 'app/entities/class-name/class-name-update.component';
import { ClassNameService } from 'app/entities/class-name/class-name.service';
import { ClassName } from 'app/shared/model/class-name.model';

describe('Component Tests', () => {
  describe('ClassName Management Update Component', () => {
    let comp: ClassNameUpdateComponent;
    let fixture: ComponentFixture<ClassNameUpdateComponent>;
    let service: ClassNameService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [ClassNameUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ClassNameUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassNameUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClassNameService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ClassName(123);
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
        const entity = new ClassName();
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
