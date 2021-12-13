import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { StudentMarkesService } from '../service/student-markes.service';

import { StudentMarkesComponent } from './student-markes.component';

describe('StudentMarkes Management Component', () => {
  let comp: StudentMarkesComponent;
  let fixture: ComponentFixture<StudentMarkesComponent>;
  let service: StudentMarkesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [StudentMarkesComponent],
    })
      .overrideTemplate(StudentMarkesComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StudentMarkesComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(StudentMarkesService);

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
    expect(comp.studentMarkes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
