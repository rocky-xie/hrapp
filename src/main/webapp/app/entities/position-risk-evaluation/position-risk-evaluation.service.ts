import axios from 'axios';

import { type IPositionRiskEvaluation } from '@/shared/model/position-risk-evaluation.model';
import buildPaginationQueryOpts from '@/shared/sort/sorts';

const baseApiUrl = 'api/position-risk-evaluations';

export default class PositionRiskEvaluationService {
  find(id: number): Promise<IPositionRiskEvaluation> {
    return new Promise<IPositionRiskEvaluation>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  retrieve(paginationQuery?: any): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}?${buildPaginationQueryOpts(paginationQuery)}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  create(entity: IPositionRiskEvaluation): Promise<IPositionRiskEvaluation> {
    return new Promise<IPositionRiskEvaluation>((resolve, reject) => {
      axios
        .post(baseApiUrl, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  evaluate(
    positionId: number,
    documentStatus?: string | null,
    customerOrSystemDependency?: string | null,
    successionReadiness?: string | null,
    preview: boolean = false,
  ): Promise<IPositionRiskEvaluation> {
    const params = {
      ...(documentStatus ? { documentStatus } : {}),
      ...(customerOrSystemDependency ? { customerOrSystemDependency } : {}),
      ...(successionReadiness ? { successionReadiness } : {}),
      preview,
    };

    return new Promise<IPositionRiskEvaluation>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}/evaluate/${positionId}`, null, { params })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  update(entity: IPositionRiskEvaluation): Promise<IPositionRiskEvaluation> {
    return new Promise<IPositionRiskEvaluation>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  partialUpdate(entity: IPositionRiskEvaluation): Promise<IPositionRiskEvaluation> {
    return new Promise<IPositionRiskEvaluation>((resolve, reject) => {
      axios
        .patch(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
