import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { BusRouteUpdateComponent } from 'app/entities/bus-route/bus-route-update.component';
import { BusRouteService } from 'app/entities/bus-route/bus-route.service';
import { BusRoute } from 'app/shared/model/bus-route.model';

describe('Component Tests', () => {
  describe('BusRoute Management Update Component', () => {
    let comp: BusRouteUpdateComponent;
    let fixture: ComponentFixture<BusRouteUpdateComponent>;
    let service: BusRouteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [BusRouteUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BusRouteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BusRouteUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BusRouteService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BusRoute(123);
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
        const entity = new BusRoute();
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
