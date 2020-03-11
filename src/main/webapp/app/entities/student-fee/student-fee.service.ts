import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IStudentFee } from 'app/shared/model/student-fee.model';

type EntityResponseType = HttpResponse<IStudentFee>;
type EntityArrayResponseType = HttpResponse<IStudentFee[]>;

@Injectable({ providedIn: 'root' })
export class StudentFeeService {
  public resourceUrl = SERVER_API_URL + 'api/student-fees';

  constructor(protected http: HttpClient) {}

  create(studentFee: IStudentFee): Observable<EntityResponseType> {
    return this.http.post<IStudentFee>(this.resourceUrl, studentFee, { observe: 'response' });
  }

  update(studentFee: IStudentFee): Observable<EntityResponseType> {
    return this.http.put<IStudentFee>(this.resourceUrl, studentFee, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStudentFee>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStudentFee[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
