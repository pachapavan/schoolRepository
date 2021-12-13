import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { StudentFeeService } from '../service/student-fee.service';

import { StudentFeeComponent } from './student-fee.component';

describe('StudentFee Management Component', () => {
  let comp: StudentFeeComponent;
  let fixture: ComponentFixture<StudentFeeComponent>;
  let service: StudentFeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [StudentFeeComponent],
    })
      .overrideTemplate(StudentFeeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StudentFeeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(StudentFeeService);

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
    expect(comp.studentFees?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
