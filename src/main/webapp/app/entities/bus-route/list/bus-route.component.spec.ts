import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BusRouteService } from '../service/bus-route.service';

import { BusRouteComponent } from './bus-route.component';

describe('BusRoute Management Component', () => {
  let comp: BusRouteComponent;
  let fixture: ComponentFixture<BusRouteComponent>;
  let service: BusRouteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BusRouteComponent],
    })
      .overrideTemplate(BusRouteComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusRouteComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BusRouteService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.busRoutes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
