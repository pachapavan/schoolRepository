jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BusRouteNameService } from '../service/bus-route-name.service';
import { IBusRouteName, BusRouteName } from '../bus-route-name.model';
import { IBusRoute } from 'app/entities/bus-route/bus-route.model';
import { BusRouteService } from 'app/entities/bus-route/service/bus-route.service';

import { BusRouteNameUpdateComponent } from './bus-route-name-update.component';

describe('BusRouteName Management Update Component', () => {
  let comp: BusRouteNameUpdateComponent;
  let fixture: ComponentFixture<BusRouteNameUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let busRouteNameService: BusRouteNameService;
  let busRouteService: BusRouteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BusRouteNameUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(BusRouteNameUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusRouteNameUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    busRouteNameService = TestBed.inject(BusRouteNameService);
    busRouteService = TestBed.inject(BusRouteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call BusRoute query and add missing value', () => {
      const busRouteName: IBusRouteName = { id: 456 };
      const busRoute: IBusRoute = { id: 73129 };
      busRouteName.busRoute = busRoute;

      const busRouteCollection: IBusRoute[] = [{ id: 63122 }];
      jest.spyOn(busRouteService, 'query').mockReturnValue(of(new HttpResponse({ body: busRouteCollection })));
      const additionalBusRoutes = [busRoute];
      const expectedCollection: IBusRoute[] = [...additionalBusRoutes, ...busRouteCollection];
      jest.spyOn(busRouteService, 'addBusRouteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ busRouteName });
      comp.ngOnInit();

      expect(busRouteService.query).toHaveBeenCalled();
      expect(busRouteService.addBusRouteToCollectionIfMissing).toHaveBeenCalledWith(busRouteCollection, ...additionalBusRoutes);
      expect(comp.busRoutesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const busRouteName: IBusRouteName = { id: 456 };
      const busRoute: IBusRoute = { id: 48170 };
      busRouteName.busRoute = busRoute;

      activatedRoute.data = of({ busRouteName });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(busRouteName));
      expect(comp.busRoutesSharedCollection).toContain(busRoute);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusRouteName>>();
      const busRouteName = { id: 123 };
      jest.spyOn(busRouteNameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ busRouteName });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: busRouteName }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(busRouteNameService.update).toHaveBeenCalledWith(busRouteName);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusRouteName>>();
      const busRouteName = new BusRouteName();
      jest.spyOn(busRouteNameService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ busRouteName });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: busRouteName }));
      saveSubject.complete();

      // THEN
      expect(busRouteNameService.create).toHaveBeenCalledWith(busRouteName);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusRouteName>>();
      const busRouteName = { id: 123 };
      jest.spyOn(busRouteNameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ busRouteName });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(busRouteNameService.update).toHaveBeenCalledWith(busRouteName);
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
