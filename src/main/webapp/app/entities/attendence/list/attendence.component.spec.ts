import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AttendenceService } from '../service/attendence.service';

import { AttendenceComponent } from './attendence.component';

describe('Attendence Management Component', () => {
  let comp: AttendenceComponent;
  let fixture: ComponentFixture<AttendenceComponent>;
  let service: AttendenceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AttendenceComponent],
    })
      .overrideTemplate(AttendenceComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttendenceComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AttendenceService);

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
    expect(comp.attendences?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
