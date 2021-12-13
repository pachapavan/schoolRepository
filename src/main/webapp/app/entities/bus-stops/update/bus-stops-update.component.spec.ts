jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BusStopsService } from '../service/bus-stops.service';
import { IBusStops, BusStops } from '../bus-stops.model';
import { IBusRoute } from 'app/entities/bus-route/bus-route.model';
import { BusRouteService } from 'app/entities/bus-route/service/bus-route.service';

import { BusStopsUpdateComponent } from './bus-stops-update.component';

describe('BusStops Management Update Component', () => {
  let comp: BusStopsUpdateComponent;
  let fixture: ComponentFixture<BusStopsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let busStopsService: BusStopsService;
  let busRouteService: BusRouteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BusStopsUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(BusStopsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusStopsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    busStopsService = TestBed.inject(BusStopsService);
    busRouteService = TestBed.inject(BusRouteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call BusRoute query and add missing value', () => {
      const busStops: IBusStops = { id: 456 };
      const busRoute: IBusRoute = { id: 39493 };
      busStops.busRoute = busRoute;

      const busRouteCollection: IBusRoute[] = [{ id: 74812 }];
      jest.spyOn(busRouteService, 'query').mockReturnValue(of(new HttpResponse({ body: busRouteCollection })));
      const additionalBusRoutes = [busRoute];
      const expectedCollection: IBusRoute[] = [...additionalBusRoutes, ...busRouteCollection];
      jest.spyOn(busRouteService, 'addBusRouteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ busStops });
      comp.ngOnInit();

      expect(busRouteService.query).toHaveBeenCalled();
      expect(busRouteService.addBusRouteToCollectionIfMissing).toHaveBeenCalledWith(busRouteCollection, ...additionalBusRoutes);
      expect(comp.busRoutesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const busStops: IBusStops = { id: 456 };
      const busRoute: IBusRoute = { id: 77836 };
      busStops.busRoute = busRoute;

      activatedRoute.data = of({ busStops });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(busStops));
      expect(comp.busRoutesSharedCollection).toContain(busRoute);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusStops>>();
      const busStops = { id: 123 };
      jest.spyOn(busStopsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ busStops });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: busStops }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(busStopsService.update).toHaveBeenCalledWith(busStops);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusStops>>();
      const busStops = new BusStops();
      jest.spyOn(busStopsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ busStops });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: busStops }));
      saveSubject.complete();

      // THEN
      expect(busStopsService.create).toHaveBeenCalledWith(busStops);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusStops>>();
      const busStops = { id: 123 };
      jest.spyOn(busStopsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ busStops });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(busStopsService.update).toHaveBeenCalledWith(busStops);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackBusRouteById', () => {
      it('Should return tracked BusRoute primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBusRouteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
