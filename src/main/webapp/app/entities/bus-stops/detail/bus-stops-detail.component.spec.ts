import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BusStopsDetailComponent } from './bus-stops-detail.component';

describe('BusStops Management Detail Component', () => {
  let comp: BusStopsDetailComponent;
  let fixture: ComponentFixture<BusStopsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BusStopsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ busStops: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BusStopsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BusStopsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load busStops on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.busStops).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
