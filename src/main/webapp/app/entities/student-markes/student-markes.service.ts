import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IStudentMarkes } from 'app/shared/model/student-markes.model';

type EntityResponseType = HttpResponse<IStudentMarkes>;
type EntityArrayResponseType = HttpResponse<IStudentMarkes[]>;

@Injectable({ providedIn: 'root' })
export class StudentMarkesService {
  public resourceUrl = SERVER_API_URL + 'api/student-markes';

  constructor(protected http: HttpClient) {}

  create(studentMarkes: IStudentMarkes): Observable<EntityResponseType> {
    return this.http.post<IStudentMarkes>(this.resourceUrl, studentMarkes, { observe: 'response' });
  }

  update(studentMarkes: IStudentMarkes): Observable<EntityResponseType> {
    return this.http.put<IStudentMarkes>(this.resourceUrl, studentMarkes, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStudentMarkes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStudentMarkes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
