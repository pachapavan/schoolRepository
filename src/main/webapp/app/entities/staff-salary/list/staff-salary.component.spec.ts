import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { StaffSalaryService } from '../service/staff-salary.service';

import { StaffSalaryComponent } from './staff-salary.component';

describe('StaffSalary Management Component', () => {
  let comp: StaffSalaryComponent;
  let fixture: ComponentFixture<StaffSalaryComponent>;
  let service: StaffSalaryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [StaffSalaryComponent],
    })
      .overrideTemplate(StaffSalaryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StaffSalaryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(StaffSalaryService);

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
    expect(comp.staffSalaries?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
