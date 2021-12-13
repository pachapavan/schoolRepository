import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BusStopsService } from '../service/bus-stops.service';

import { BusStopsComponent } from './bus-stops.component';

describe('BusStops Management Component', () => {
  let comp: BusStopsComponent;
  let fixture: ComponentFixture<BusStopsComponent>;
  let service: BusStopsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BusStopsComponent],
    })
      .overrideTemplate(BusStopsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusStopsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BusStopsService);

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
    expect(comp.busStops?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
