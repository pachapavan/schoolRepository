import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { BusStopsUpdateComponent } from 'app/entities/bus-stops/bus-stops-update.component';
import { BusStopsService } from 'app/entities/bus-stops/bus-stops.service';
import { BusStops } from 'app/shared/model/bus-stops.model';

describe('Component Tests', () => {
  describe('BusStops Management Update Component', () => {
    let comp: BusStopsUpdateComponent;
    let fixture: ComponentFixture<BusStopsUpdateComponent>;
    let service: BusStopsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [BusStopsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BusStopsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BusStopsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BusStopsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BusStops(123);
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
        const entity = new BusStops();
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
