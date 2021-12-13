import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SectionService } from '../service/section.service';

import { SectionComponent } from './section.component';

describe('Section Management Component', () => {
  let comp: SectionComponent;
  let fixture: ComponentFixture<SectionComponent>;
  let service: SectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SectionComponent],
    })
      .overrideTemplate(SectionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SectionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SectionService);

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
    expect(comp.sections?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
