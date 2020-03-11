import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { BusRouteNameUpdateComponent } from 'app/entities/bus-route-name/bus-route-name-update.component';
import { BusRouteNameService } from 'app/entities/bus-route-name/bus-route-name.service';
import { BusRouteName } from 'app/shared/model/bus-route-name.model';

describe('Component Tests', () => {
  describe('BusRouteName Management Update Component', () => {
    let comp: BusRouteNameUpdateComponent;
    let fixture: ComponentFixture<BusRouteNameUpdateComponent>;
    let service: BusRouteNameService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [BusRouteNameUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BusRouteNameUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BusRouteNameUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BusRouteNameService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BusRouteName(123);
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
        const entity = new BusRouteName();
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
