import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BusRouteNameService } from '../service/bus-route-name.service';

import { BusRouteNameComponent } from './bus-route-name.component';

describe('BusRouteName Management Component', () => {
  let comp: BusRouteNameComponent;
  let fixture: ComponentFixture<BusRouteNameComponent>;
  let service: BusRouteNameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BusRouteNameComponent],
    })
      .overrideTemplate(BusRouteNameComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusRouteNameComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BusRouteNameService);

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
    expect(comp.busRouteNames?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
