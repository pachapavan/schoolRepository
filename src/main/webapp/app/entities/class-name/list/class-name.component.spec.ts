import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ClassNameService } from '../service/class-name.service';

import { ClassNameComponent } from './class-name.component';

describe('ClassName Management Component', () => {
  let comp: ClassNameComponent;
  let fixture: ComponentFixture<ClassNameComponent>;
  let service: ClassNameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ClassNameComponent],
    })
      .overrideTemplate(ClassNameComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClassNameComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ClassNameService);

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
    expect(comp.classNames?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
